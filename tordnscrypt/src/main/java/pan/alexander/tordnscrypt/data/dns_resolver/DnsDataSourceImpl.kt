/*
    This file is part of InviZible Pro.

    InviZible Pro is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    InviZible Pro is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with InviZible Pro.  If not, see <http://www.gnu.org/licenses/>.

    Copyright 2019-2023 by Garmatin Oleksandr invizible.soft@gmail.com
 */

package pan.alexander.tordnscrypt.data.dns_resolver

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import pan.alexander.tordnscrypt.utils.Constants.*
import pan.alexander.tordnscrypt.utils.dns.*
import java.net.URL
import javax.inject.Inject

class DnsDataSourceImpl @Inject constructor(
    private val udpResolverFactory: UdpResolverFactory,
    private val dohResolverFactory: DohResolverFactory
) : DnsDataSource {

    override fun resolveDomainUDP(
        domain: String,
        includeIPv6: Boolean,
        port: Int,
        timeout: Int
    ): Array<Record>? {
        val domainVerified = Domain(URL(domain).host ?: "")
        return if (includeIPv6) {
            (resolveDomainUDPIPv4(domainVerified, port, timeout) ?: emptyArray()) +
                    (resolveDomainUDPIPv6(domainVerified, port, timeout) ?: emptyArray())
        } else {
            resolveDomainUDPIPv4(domainVerified, port, timeout)
        }
    }


    private fun resolveDomainUDPIPv4(
        domain: Domain,
        port: Int,
        timeout: Int
    ): Array<Record>? {
        return udpResolverFactory.createUdpResolver(
            LOOPBACK_ADDRESS,
            port,
            Record.TYPE_A,
            timeout
        ).resolve(domain)
    }

    private fun resolveDomainUDPIPv6(
        domain: Domain,
        port: Int,
        timeout: Int
    ): Array<Record>? {
        return udpResolverFactory.createUdpResolver(
            LOOPBACK_ADDRESS,
            port,
            Record.TYPE_AAAA,
            timeout
        ).resolve(domain)
    }

    override fun resolveDomainDOH(
        domain: String,
        includeIPv6: Boolean,
        timeout: Int
    ): Array<Record>? {
        val domainVerified = Domain(URL(domain).host ?: "")
        return if (includeIPv6) {
            (resolveDomainDOHIPv4(domainVerified, timeout) ?: emptyArray()) +
                    (resolveDomainDOHIPv6(domainVerified, timeout) ?: emptyArray())
        } else {
            resolveDomainDOHIPv4(domainVerified, timeout)
        }
    }

    private fun resolveDomainDOHIPv4(
        domain: Domain,
        timeout: Int
    ): Array<Record>? {
        return dohResolverFactory.createDohResolver(
            QUAD_DOH_SERVER,
            Record.TYPE_A,
            timeout
        ).resolve(domain)
    }

    private fun resolveDomainDOHIPv6(
        domain: Domain,
        timeout: Int
    ): Array<Record>? {
        return dohResolverFactory.createDohResolver(
            QUAD_DOH_SERVER,
            Record.TYPE_AAAA,
            timeout
        ).resolve(domain)
    }

    override fun reverseResolveUDP(
        ip: String,
        port: Int,
        timeout: Int
    ): Array<Record>? {
        return udpResolverFactory.createUdpResolver(
            LOOPBACK_ADDRESS,
            port,
            Record.TYPE_PTR,
            timeout
        ).reverseResolve(ip)
    }

    override fun reverseResolveDOH(
        ip: String,
        timeout: Int
    ): Array<Record>? {
        return dohResolverFactory.createDohResolver(
            QUAD_DOH_SERVER,
            Record.TYPE_PTR,
            timeout
        ).reverseResolve(ip)
    }

    @AssistedFactory
    interface UdpResolverFactory {
        fun createUdpResolver(
            domain: String,
            @Assisted("port") port: Int,
            @Assisted("type") type: Int,
            @Assisted("timeout") timeout: Int = Resolver.DNS_DEFAULT_TIMEOUT_SEC
        ): UdpResolver
    }

    @AssistedFactory
    interface DohResolverFactory {
        fun createDohResolver(
            domain: String,
            @Assisted("type") type: Int,
            @Assisted("timeout") timeout: Int = Resolver.DNS_DEFAULT_TIMEOUT_SEC
        ): DohResolver
    }
}
